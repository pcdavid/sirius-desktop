/*******************************************************************************
 * Copyright (c) 2018 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *******************************************************************************/

export const testEMFGraphQLServices = async url => {
  // Get data for different tests
  const testsData = [];
  testsData.push(getEPackageTestData());
  testsData.push(getEPackagesTestData());
  
  const testResults = [];
  for (let index = 0; index < testsData.length; index++) {
    let result = '';
    let startingTime = new Date().getTime();
    const testData = testsData[index];
    const { title, request, expectedJson } = testData;
    let status = "Failure";

    //Call the GraphQL http service
    // TODO: ajouter un try catch pour éviter de tout planter si une seule requete foire
    // TODO advanced : faire en sorte d'afficher les résultats au fur et à mesure
    try {
      const response = await fetch(url, {
        method: "POST",
        body: JSON.stringify(request)
      });
      const resultJson = await response.json();

      if (resultJson.data !== undefined && JSON.stringify(resultJson.data) === JSON.stringify(expectedJson)) {
        status = "Success";
      }
      result = JSON.stringify(resultJson.data, null, 2);
    } catch (error) {
      result = 'No result\n'+ error;
    }
    
    const message = 'Duration: ' + (new Date().getTime() - startingTime) + " ms";
    testResults.push({
      index,
      title,
      status,
      message,
      query: request.query,
      expected: JSON.stringify(expectedJson, null, 2),
      result
    });
  }

  return testResults;
};

function getEPackageTestData() {
  const queryEPackage = `
query getEPackage($nsURI: String!) {
  viewer{
    ePackage(nsURI:$nsURI){
      name,
      nsURI,
      nsPrefix,
      eFactoryInstance{
        fragment
      },
      fragment,
      eSubpackages{
        name
      },
      eSuperPackage{
        name
      }
    }
  }
}
  `.trim();
  const operationEPackage = "getEPackage";
  const requestEPackage = {
    variables: { nsURI: "http://www.eclipse.org/emf/2002/Ecore" },
    query: queryEPackage,
    operationName: operationEPackage
  };
  const expectedJsonEPackage = {
    viewer: {
      ePackage: {
        name: "ecore",
        nsURI: "http://www.eclipse.org/emf/2002/Ecore",
        nsPrefix: "ecore",
        eFactoryInstance: {},
        fragment: "/",
        eSubpackages: []
      }
    }
  };
  return {
    title: "getEPackageTest",
    request: requestEPackage,
    expectedJson: expectedJsonEPackage
  };
}

function getEPackagesTestData() {
  const queryEPackages = `
query getEPackages($first: Int!, $after: String) {
  viewer{
    ePackages(first:$first, after:$after){
      totalCount
    }
  }
}
  `.trim();
  const operationEPackages = "getEPackages";
  const requestEPackages = {
    variables: { first: 2, after: null },
    query: queryEPackages,
    operationName: operationEPackages
  };
  //const expectedJson = '"viewer": { "ePackages": {  "totalCount": 88,   "pageInfo": {   "hasNextPage": true      }    }';
  const expectedJsonEPackages = {
    viewer: {
      ePackages: {
        totalCount: 88
      }
    }
  };
  return {
    title: "getEPackagesTest",
    request: requestEPackages,
    expectedJson: expectedJsonEPackages
  };
}
