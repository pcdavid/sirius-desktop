/*******************************************************************************
 * Copyright (c) 2018 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *******************************************************************************/
import React, { Component } from 'react';

import { css } from 'react-emotion';

import { Result } from '../result/Result';
import { testGraphQLServices } from '../../services/testGraphQLServices';
import { testEMFGraphQLServices } from '../../services/testEMFGraphQLServices';

const mainStyle = css`
  display: grid;
  grid-template-columns: 1fr;
  grid-auto-rows: min-content;
  grid-row-gap: 16px;
  border-radius: 5px;
`;

export class Main extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tests: []
    };
  }

  async componentDidUpdate(prevProps) {
    if (prevProps.url !== this.props.url) {
    //if (this.props.url != null) {
      const {url} = this.props;
      const tests = await testEMFGraphQLServices(url);

      this.setState({tests});
    }
  }

  render() {
    const {tests} = this.state;
    return (
      <div className={mainStyle}>
        {tests.map(result => (
          <Result
            key={result.index}
            title={result.title}
            status={result.status}
            message={result.message}
            query={result.query}
            expected={result.expected}
            result={result.result}
          />
        ))}
      </div>
    );
  }
}
