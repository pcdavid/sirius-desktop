/*******************************************************************************
 * Copyright (c) 2018 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *******************************************************************************/
import React, { Component } from "react";
import { css } from "react-emotion";

const resultStyle = css({
  backgroundColor: "#ffffff",
  boxShadow: "0 1px 4px 0 rgba(31, 45, 61, 0.15)",
  padding: "8px 16px",
  borderRadius: "5px",
});
const hiddenBodyStyle = css({
  opacity: 0,
  height: 0,
});
export class Result extends Component {
  constructor(props){
    super(props);
    this.state = {expanded: false};
    this.handleClick = this.handleClick.bind(this);
  }

  render() {
    const { title, status, message, query, expected, result } = this.props;
    const {expanded} = this.state;

    let body = <Body className={hiddenBodyStyle} query={query} expected={expected} result={result} />;
    if (expanded) {
      body = <Body query={query} expected={expected} result={result} />;
    }

    return (
      <div className={resultStyle}>
        <Header title={title} status={status} />
        <Message expanded={expanded} content={message} onClick={this.handleClick} />
        {body}
      </div>
    );
  }

  handleClick() {
    // Passer une fontion pour la mise à jour d'un état à partir de l'état précédent. On évite le problème d'accès concurrent sur state en utilisant une fonction.
    this.setState((prevState) => {
      return {expanded: !prevState.expanded};
    });
  }
}

const headerStyle = css({
  display: 'flex',
  flexDirection: 'row',
  justifyContent: 'space-between',
  alignItems: 'center',
});
const headerTitleStyle = css({
  fontSize: '18px',
  lineHeight: '22px',
  fontWeight: '600'
});
const SUCCESS = 'hsl(124, 57%, 60%)';
const SUCCESS_BORDER = '2px solid hsl(124, 57%, 40%)';
const FAILURE = 'hsl(2,83%,60%)';
const FAILURE_BORDER = '2px solid hsl(2,83%,40%)';

const Header = ({ title, status }) => {
  const headerStatusStyle = css({
    fontSize: '18px',
    lineHeight: '22px',
    fontWeight: '600',
    backgroundColor: status === 'Success' ? SUCCESS : FAILURE,
    border: status === 'Success' ? SUCCESS_BORDER : FAILURE_BORDER,
    color: 'white',
    borderRadius: '3px',
    padding: '3px 8px',
  });
  return (
    <div className={headerStyle}>
      <h2 className={headerTitleStyle}>{title}</h2>
      <h3 className={headerStatusStyle}>{status}</h3>
    </div>
  );
};

const messageStyle = css({
  display: 'flex',
  flexDirection: 'row',
  alignItems: 'center',
});
const buttonStyle = css({
  fontSize: '22px',
  marginLeft: '-5px',
});
const Message = ({ expanded, content, onClick }) => {
  const label = expanded ? '\u23f7' : '\u23f5';
  return (
    <div className={messageStyle}>
      <button className={buttonStyle} onClick={onClick}>{label}</button>
      <p onClick={onClick}>{content}</p>
    </div>
  );
};

const bodyStyle = css({
  display: 'grid',
  gridTemplateColumns: '1fr 1fr',
  gridRowGap: '8px',
  gridColumnGap: '8px',
  padding: '16px 0px',
  transition: 'height 1s, opacity 0.5s'
});
const queryStyle = css({
  gridColumnStart: '1',
  gridColumnEnd: '3',
});
const Body = ({ query, expected, result, className }) => {
  let bodyClassName = bodyStyle;
  if (className) {
    bodyClassName =  className;
  }
  return (
    <div className={bodyClassName}>
      <Viewer className={queryStyle} title="Query:" content={query} />
      <Viewer title="Expected:" content={expected} />
      <Viewer title="Result:" content={result} />
    </div>
  );
};

const viewerStyle = css({
  display: 'grid',
  gridTemplateRows: 'min-content 1fr',
  gridTemplateColumns: '1fr',
  gridRowGap: '8px',
});
const preStyle = css({
  border: '1px solid black',
  padding: '4px',
  overflowX: 'auto',
  overflowY: 'hidden',
  borderRadius: '3px'
});
const Viewer = ({ title, content, className, ...props }) => {
  let viewerClassName = viewerStyle;
  if (className) {
    viewerClassName = viewerClassName + ' ' + className;
  }
  return (
    <div className={viewerClassName} {...props}>
      <h4>{title}</h4>
      <pre className={preStyle}>{content}</pre>
    </div>
  );
};
