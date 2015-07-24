/*******************************************************************************
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.common.acceleo.aql.business.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.acceleo.query.runtime.AcceleoQueryEvaluationException;
import org.eclipse.acceleo.query.runtime.AcceleoQueryValidationException;
import org.eclipse.acceleo.query.runtime.CrossReferenceProvider;
import org.eclipse.acceleo.query.runtime.EvaluationResult;
import org.eclipse.acceleo.query.runtime.IQueryBuilderEngine;
import org.eclipse.acceleo.query.runtime.IQueryBuilderEngine.AstResult;
import org.eclipse.acceleo.query.runtime.IQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IQueryEvaluationEngine;
import org.eclipse.acceleo.query.runtime.IQueryValidationEngine;
import org.eclipse.acceleo.query.runtime.IValidationMessage;
import org.eclipse.acceleo.query.runtime.IValidationResult;
import org.eclipse.acceleo.query.runtime.InvalidAcceleoPackageException;
import org.eclipse.acceleo.query.runtime.Query;
import org.eclipse.acceleo.query.runtime.QueryEvaluation;
import org.eclipse.acceleo.query.runtime.QueryParsing;
import org.eclipse.acceleo.query.runtime.QueryValidation;
import org.eclipse.acceleo.query.validation.type.EClassifierType;
import org.eclipse.acceleo.query.validation.type.IType;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.codegen.ecore.genmodel.GenClassifier;
import org.eclipse.emf.codegen.ecore.genmodel.GenDataType;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenPackage;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.sirius.common.acceleo.aql.business.AQLSiriusPlugin;
import org.eclipse.sirius.common.acceleo.aql.business.api.AQLConstants;
import org.eclipse.sirius.common.acceleo.aql.business.api.ExpressionTrimmer;
import org.eclipse.sirius.common.acceleo.aql.business.api.TypesUtil;
import org.eclipse.sirius.common.tools.api.interpreter.ClassLoadingCallback;
import org.eclipse.sirius.common.tools.api.interpreter.EPackageLoadingCallback;
import org.eclipse.sirius.common.tools.api.interpreter.EvaluationException;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterContext;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterStatus;
import org.eclipse.sirius.common.tools.api.interpreter.InterpreterStatusFactory;
import org.eclipse.sirius.common.tools.api.interpreter.ValidationResult;
import org.eclipse.sirius.common.tools.api.interpreter.VariableType;
import org.eclipse.sirius.ecore.extender.business.api.accessor.EcoreMetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.MetamodelDescriptor;
import org.eclipse.sirius.ecore.extender.business.api.accessor.ModelAccessor;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * A Sirius interpreter using the Acceleo Query Language. It only supports
 * expressions which are not using implicit variables.
 *
 * @author cedric
 */
public class AQLSiriusInterpreter extends AcceleoAbstractInterpreter {

    private LoadingCache<String, AstResult> parsedExpressions;

    private IQueryEnvironment queryEnvironment;

    private ECrossReferenceAdapter siriusXref;

    /**
     * Mapping from {@link Class} qualified name to {@link EClassifier} for
     * {@link EClassifier} loaded from the workspace.
     */
    private final Map<String, EClassifier> classifierToEClass = Maps.newHashMap();

    private CrossReferenceProvider xRef = new CrossReferenceProvider() {

        @Override
        public Collection<Setting> getInverseReferences(EObject self) {
            if (siriusXref != null) {
                return siriusXref.getInverseReferences(self);

            } else {
                return Collections.emptySet();
            }
        }
    };

    private final ClassLoadingCallback callback = new ClassLoadingCallback() {

        @Override
        public void loaded(String qualifiedName, Class<?> clazz) {
            final EClassifier eCls = classifierToEClass.get(qualifiedName);
            if (eCls == null) {
                try {
                    queryEnvironment.registerServicePackage(clazz);
                } catch (InvalidAcceleoPackageException e) {
                    AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Error loading Java extension class " + qualifiedName + " :" + e.getMessage(),
                            e));
                }
            } else {
                queryEnvironment.registerCustomClassMapping(eCls, clazz);
            }

        }

        @Override
        public void notFound(String qualifiedName) {
            final EClassifier eCls = classifierToEClass.get(qualifiedName);
            if (eCls == null) {
                AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Could not find Java extension class " + qualifiedName));
            } else {
                AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Could not find EClassifier class " + qualifiedName));
            }
        }

        @Override
        public void unloaded(String qualifiedName, Class<?> clazz) {
            final EClassifier eCls = classifierToEClass.get(qualifiedName);
            if (eCls == null) {
                queryEnvironment.removeServicePackage(clazz);
            } else {
                queryEnvironment.registerCustomClassMapping(eCls, null);
            }
        }
    };

    private final EPackageLoadingCallback ePackageCallBack;

    /**
     * Create a new interpreter supporting the AQL evaluation engine.
     */
    public AQLSiriusInterpreter() {
        super();
        this.queryEnvironment = Query.newEnvironmentWithDefaultServices(xRef);
        this.ePackageCallBack = new EPackageLoadingCallback() {

            @Override
            public void loaded(String nsURI, EPackage pak) {
                registerEPackage(queryEnvironment, pak);
            }

            @Override
            public void unloaded(String nsURI, EPackage pak) {
                unregisterEPackage(queryEnvironment, pak);
            }
        };
        this.javaExtensions.addClassLoadingCallBack(callback);
        this.javaExtensions.addEPackageCallBack(ePackageCallBack);
        final IQueryBuilderEngine builder = QueryParsing.newBuilder(queryEnvironment);
        this.parsedExpressions = CacheBuilder.newBuilder().maximumSize(500).build(new CacheLoader<String, AstResult>() {

            @Override
            public AstResult load(String key) throws Exception {
                return builder.build(key);
            }

        });
        this.queryEnvironment.registerEPackage(EcorePackage.eINSTANCE);
        this.queryEnvironment.registerCustomClassMapping(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class);
    }

    @Override
    public void activateMetamodels(Collection<MetamodelDescriptor> metamodels) {
        Set<EPackage> additionalEPackages = Sets.newLinkedHashSet();
        for (MetamodelDescriptor descriptor : metamodels) {
            if (descriptor instanceof EcoreMetamodelDescriptor) {
                EPackage pkg = ((EcoreMetamodelDescriptor) descriptor).resolve();
                if (pkg != null) {
                    additionalEPackages.add(pkg);
                }
            }
        }
        for (EPackage ePackage : additionalEPackages) {
            registerEPackage(this.queryEnvironment, ePackage);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.javaExtensions.removeClassLoadingCallBack(callback);
        this.javaExtensions.removeEPackageCallBack(ePackageCallBack);
    }

    @Override
    public Object evaluate(EObject target, String fullExpression) throws EvaluationException {
        this.javaExtensions.reloadIfNeeded();
        String expression = new ExpressionTrimmer(fullExpression).getExpression();
        Map<String, Object> variables = getVariables();
        variables.put("self", target); //$NON-NLS-1$
        AstResult build;
        try {
            build = parsedExpressions.get(expression);
            IQueryEvaluationEngine evaluationEngine = QueryEvaluation.newEngine(queryEnvironment);
            EvaluationResult evalResult = evaluationEngine.eval(build, variables);
            if (evalResult.getDiagnostic().getSeverity() == Diagnostic.ERROR) {
                throw new EvaluationException(evalResult.getDiagnostic().getMessage(), evalResult.getDiagnostic().getException());
            }
            return evalResult.getResult();
        } catch (ExecutionException e) {
            throw new EvaluationException(e.getCause());
        }
    }

    @Override
    public String getVariablePrefix() {
        /*
         * no variable prefix for this interpreter.
         */
        return null;
    }

    @Override
    public void setCrossReferencer(ECrossReferenceAdapter crossReferencer) {
        this.siriusXref = crossReferencer;
    }

    @Override
    public void setModelAccessor(ModelAccessor modelAccessor) {
        /*
         * AQL does not support the ModelAccessor yet.
         */
    }

    @Override
    public boolean supportsValidation() {
        return true;
    }

    @Override
    public ValidationResult analyzeExpression(IInterpreterContext context, String fullExpression) {
        this.javaExtensions.reloadIfNeeded();

        String trimmedExpression = new ExpressionTrimmer(fullExpression).getExpression();
        ValidationResult result = new ValidationResult();

        Map<String, Set<IType>> variableTypes = TypesUtil.createAQLVariableTypesFromInterpreterContext(context, queryEnvironment);

        IQueryValidationEngine validator = QueryValidation.newEngine(this.queryEnvironment);
        try {
            IValidationResult validationResult = validator.validate(trimmedExpression, variableTypes);
            for (IValidationMessage message : validationResult.getMessages()) {
                result.addStatus(InterpreterStatusFactory.createInterpreterStatus(context, IInterpreterStatus.WARNING, message.getMessage()));
            }
            List<String> classifierNames = Lists.newArrayList();
            for (IType type : validationResult.getPossibleTypes(validationResult.getAstResult().getAst())) {
                if (type instanceof EClassifierType) {
                    EClassifierType eClassifierType = (EClassifierType) type;
                    if (eClassifierType.getType() != null && eClassifierType.getType().getName() != null) {
                        String typeName = eClassifierType.getType().getName();
                        if (eClassifierType.getType().getEPackage() != null && eClassifierType.getType().getEPackage().getName() != null) {
                            typeName = eClassifierType.getType().getEPackage().getName() + "." + typeName; //$NON-NLS-1$
                        }
                        classifierNames.add(typeName);
                    }
                }
                result.setReturnType(VariableType.fromStrings(classifierNames));
            }
        } catch (AcceleoQueryValidationException e) {
            result.addStatus(InterpreterStatusFactory.createInterpreterStatus(context, IInterpreterStatus.ERROR, e.getMessage()));
            AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.ERROR, AQLSiriusPlugin.INSTANCE.getSymbolicName(), e.getMessage(), e));
        } catch (AcceleoQueryEvaluationException e) {
            result.addStatus(InterpreterStatusFactory.createInterpreterStatus(context, IInterpreterStatus.ERROR, e.getMessage()));
        }

        return result;
    }

    /**
     * return the cross reference provider used by this interpreter instance.
     * 
     * @return the cross reference provider used by this interpreter instance.
     */
    public CrossReferenceProvider getCrossReferenceProvider() {
        return xRef;
    }

    @Override
    public String getPrefix() {
        return AQLConstants.AQL_PREFIX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean provides(String expression) {
        return expression != null && expression.startsWith(AQLConstants.AQL_PREFIX);
    }

    /**
     * The query environment currently used by this interpreter.
     * 
     * @return The query environment currently used by this interpreter.
     */
    public IQueryEnvironment getQueryEnvironment() {
        /*
         * The JavaExtensionManager might impact the query environment when
         * loading classes. We trigger the reload before returning the
         * IQueryEnvironment so that it is properly configured with EPackages
         * and imports.
         */
        this.javaExtensions.reloadIfNeeded();

        return this.queryEnvironment;
    }

    /**
     * Registers the given {@link EPackage} to the given
     * {@link IQueryEnvironment}.
     * 
     * @param queryEnvironment
     *            the {@link IQueryEnvironment}
     * @param ePackage
     *            the {@link EPackage}
     */
    public void registerEPackage(IQueryEnvironment queryEnvironment, EPackage ePackage) {
        queryEnvironment.registerEPackage(ePackage);
        if (isPlatformResource(ePackage)) {
            final URI genModelURI = EcorePlugin.getEPackageNsURIToGenModelLocationMap(true).get(ePackage.getNsURI());
            if (genModelURI != null) {
                final Resource genModelResource = ePackage.eResource().getResourceSet().getResource(genModelURI, true);
                if (hasGenModel(genModelResource)) {
                    GenModel genModel = (GenModel) genModelResource.getContents().get(0);
                    registerEClasses(genModel);
                } else {
                    AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Could not load GenModel for " + ePackage.getNsURI()));
                }
                genModelResource.unload();
            } else {
                AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Could not find GenModel for " + ePackage.getNsURI()));
            }
        }
    }

    /**
     * Registers the {@link Class} to {@link EClassifier} for the given
     * {@link GenModel}.
     * 
     * @param genModel
     *            the {@link GenModel}
     */
    private void registerEClasses(GenModel genModel) {
        for (GenClassifier genClassifier : getGenClasses(genModel.getGenPackages())) {
            String className = genClassifier.getImportedInstanceClassName();
            classifierToEClass.put(className, genClassifier.getEcoreClassifier());
            this.javaExtensions.addImport(className);
        }
    }

    /**
     * Unregisters the given {@link EPackage} to the given
     * {@link IQueryEnvironment}.
     * 
     * @param queryEnvironment
     *            the {@link IQueryEnvironment}
     * @param ePackage
     *            the {@link EPackage}
     */
    public void unregisterEPackage(IQueryEnvironment queryEnvironment, EPackage ePackage) {
        queryEnvironment.removeEPackage(ePackage.getName());
        if (isPlatformResource(ePackage)) {
            final URI genModelURI = EcorePlugin.getEPackageNsURIToGenModelLocationMap(true).get(ePackage.getNsURI());
            if (genModelURI != null) {
                final Resource genModelResource = ePackage.eResource().getResourceSet().getResource(genModelURI, true);
                if (hasGenModel(genModelResource)) {
                    GenModel genModel = (GenModel) genModelResource.getContents().get(0);
                    unregisterEClasses(genModel);
                } else {
                    AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Could not load GenModel for " + ePackage.getNsURI()));
                }
                genModelResource.unload();
            } else {
                AQLSiriusPlugin.INSTANCE.log(new Status(IStatus.WARNING, AQLSiriusPlugin.INSTANCE.getSymbolicName(), "Could not find GenModel for " + ePackage.getNsURI()));
            }
        }
    }

    /**
     * Tells if the given {@link Resource} contains a {@link GenModel}.
     * 
     * @param genModelResource
     *            the {@link Resource}
     * @return <code>true</code> if the given {@link Resource} contains a
     *         {@link GenModel}, <code>false</code> otherwise
     */
    private boolean hasGenModel(final Resource genModelResource) {
        return genModelResource != null && !genModelResource.getContents().isEmpty() && genModelResource.getContents().get(0) instanceof GenModel;
    }

    /**
     * Tells if the given {@link EPackage} is a platform resource.
     * 
     * @param ePackage
     *            the {@link EPackage}
     * @return <code>true</code> if the given {@link EPackage} has a file
     *         resource, <code>false</code> otherwise
     */
    private boolean isPlatformResource(EPackage ePackage) {
        return ePackage.eResource() == null || ePackage.eResource().getURI().isPlatformResource();
    }

    /**
     * Unregisters the {@link Class} to {@link EClassifier} for the given
     * {@link GenModel}.
     * 
     * @param genModel
     *            the {@link GenModel}
     */
    private void unregisterEClasses(GenModel genModel) {
        for (GenClassifier genClassifier : getGenClasses(genModel.getGenPackages())) {
            String className = genClassifier.getImportedInstanceClassName();
            this.javaExtensions.removeImport(className);
            classifierToEClass.remove(className);
        }
    }

    /**
     * Gets the {@link List} of all contained {@link GenClassifier} from the
     * given {@link List} of {@link GenPackage}.
     * 
     * @param genPackages
     *            the {@link List} of {@link GenPackage}
     * @return the {@link List} of all contained {@link GenClassifier} from the
     *         given {@link List} of {@link GenPackage}
     */
    private List<GenClassifier> getGenClasses(List<GenPackage> genPackages) {
        final List<GenClassifier> res = new ArrayList<GenClassifier>();

        for (GenPackage genPackage : genPackages) {
            res.addAll(genPackage.getGenClassifiers());
            res.addAll(getGenClasses(genPackage.getNestedGenPackages()));
        }

        return res;
    }
}
