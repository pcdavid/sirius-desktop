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

import { Main } from '../main/Main';
import { SearchBar } from '../searchbar/SearchBar';

// font-family est mis au niveau global pour prendre la police de l'OS par défaut
// min-height: 100vh;hauteur min = 100% taille viewport
const appStyle = css`
background-color: hsl(217.5, 33.3%, 95.3%);
min-height: 100vh;
font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol';
-webkit-font-smoothing: antialiased;
-moz-osx-font-smoothing: grayscale;
`;

const mainAreaStyle = css`
display: grid;
grid-template-columns: 
  [left] minmax(1em, 1fr)
  [center] minmax(min-content, 800px)
  [right] minmax(1em, 1fr);
grid-template-rows: min-content 1fr;
grid-row-gap: 32px;
padding-top: 32px;
padding-bottom: 64px;
`;

const containerStyle = css`
grid-column: center;
`;

const titleStyle = css`
padding-top: 10px;
padding-left: 10px;
background-color: hsl(217.5, 33.3%, 95.3%);
`;

export class App extends Component {
  constructor(props) {
    super(props);
    this.state = {url: null};
    this.onNewURL = this.onNewURL.bind(this);
  }

  render() {
    const {url} = this.state;
    return (
      <div className={appStyle}>
        <div className={titleStyle}>
          <Title/>
        </div>
        <div className={mainAreaStyle}>
          <div className={containerStyle}>
            <SearchBar onNewURL={this.onNewURL}/>
          </div>
          <div className={containerStyle}>
            <Main url={url}/>
          </div>
        </div>
      </div>
    );
  }
  onNewURL(url) {
    this.setState({ url });
  }
}

function Title() {
    return (
      <h1>Sirius integration tests</h1>
    );
}

