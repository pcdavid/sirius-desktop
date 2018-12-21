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

const searchBarStyleOld = css`
  display: grid;
  justify-items: stretch;
`;
const searchBarStyle = css({
  display: 'flex',
  flexDirection: 'row',
  justifyContent: 'space-between',
  alignItems: 'center',
  justifyItems: 'strech'
});

const inputStyle = css({
  height: '40px',
  border: '1px solid hsl(211.6,27.5%,86.5%)',
  fontSize: '20px',
  padding: '4px 10px 4px 10px',
  borderRadius: '5px',
  backgroundClip: 'padding-box',
  display: 'block',
  flexGrow: 1
});

export class SearchBar extends Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
    this.handleClick = this.handleClick.bind(this);
  }

  render() {
    return (
      <div className={searchBarStyle}>
        <input className={inputStyle} type="text" onKeyPress={this.handleChange} defaultValue="http://localhost:8080/api/graphql"/>
        {/* <button onClick={this.handleClick}>Button</button> */}
      </div>
    );
  };

  handleChange(event) {
    if (event.key === 'Enter') {
      const { onNewURL } = this.props;
      onNewURL(event.target.value);
    }
  }
  handleClick(event) {
      const { onNewURL } = this.props;
      onNewURL(event.target.value);
  }
};