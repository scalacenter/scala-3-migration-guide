/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

const React = require("react");

const CompLibrary = require("../../core/CompLibrary.js");

const highlightBlock = require("highlight.js");

const MarkdownBlock = CompLibrary.MarkdownBlock; /* Used to read markdown */
const Container = CompLibrary.Container;
const GridBlock = CompLibrary.GridBlock;

const siteConfig = require(process.cwd() + "/siteConfig.js");

function imgUrl(img) {
  return siteConfig.baseUrl + "img/" + img;
}

function docUrl(doc, language) {
  return siteConfig.baseUrl + "docs/" + (language ? language + "/" : "") + doc;
}

function pageUrl(page, language) {
  return siteConfig.baseUrl + (language ? language + "/" : "") + page;
}

class Button extends React.Component {
  render() {
    return (
      <div className="pluginWrapper buttonWrapper">
        <a className="button" href={this.props.href} target={this.props.target}>
          {this.props.children}
        </a>
      </div>
    );
  }
}

Button.defaultProps = {
  target: "_self"
};

const SplashContainer = props => (
  <div className="homeContainer">
    <div className="homeSplashFade">
      <div className="wrapper homeWrapper">{props.children}</div>
    </div>
  </div>
);

const ProjectTitle = props => (
  <h2 className="projectTitle">
    {siteConfig.title}
    <small>{siteConfig.tagline}</small>
  </h2>
);

const PromoSection = props => (
  <div className="section promoSection">
    <div className="promoRow">
      <div className="pluginRowBlock" style={{display: 'flow-root'}}>{props.children}</div>
    </div>
  </div>
);

class HomeSplash extends React.Component {
  render() {
    let language = this.props.language || "";
    return (
      <SplashContainer>
        <div className="inner">
          <ProjectTitle />
          <PromoSection>
          <img
            src={imgUrl("dotty-logo.svg")}
            width="400"
          />
            <Button href={docUrl("get-started.html", language)}>
              Get started
            </Button>
          </PromoSection>
        </div>
      </SplashContainer>
    );
  }
}

const Features = props => {
  const features = [
    {
      title: "Cross-build your library with Dotty",
      content:
        "We list incompatibilities between Scala 3 and Scala 2, and solutions for cross-compiling the source code.",
      image: imgUrl("dotty-logo.svg"),
      imageAlign: "left"
    },
    {
      title: "Dotty rewrites",
      content:
        "The Dotty compiler, has been carefully designed to ease the migration from Scala 2.13 to Scala 3.0. It comes with a handful of utilities to encourage you to cross-compile your codebase",
      image: imgUrl("dotty-logo.svg"),
      imageAlign: "right"
    },
    {
      title: "Macros",
      content:
        "The Scala 2 macros are compiler dependent. In other words, macros defined in a Scala 2 library cannot be consumed by a different compiler version. Scala 3 will break this limitation but it comes with the cost of rewriting all the macro usages of the Scala ecosystem.",
      image: imgUrl("dotty-logo.svg"),
      imageAlign: "left"
    }
  ];
  return (
    <div
      className="productShowcaseSection paddingBottom"
      style={{ textAlign: "center" }}
    >
      {features.map(feature => (
        <Block key={feature.title}>{[feature]}</Block>
      ))}
    </div>
  );
};

const Block = props => (
  <Container
    padding={["bottom", "top"]}
    id={props.id}
    background={props.background}
  >
    <GridBlock align="left" contents={props.children} layout={props.layout} />
  </Container>
);

class Index extends React.Component {
  render() {
    let language = this.props.language || "";
    return (
      <div>
        <HomeSplash language={language} />
        <div className="mainContainer">
          <Features />
        </div>
      </div>
    );
  }
}

module.exports = Index;
