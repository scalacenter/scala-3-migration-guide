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

const Logo = (props) => (
  <div className="projectLogo">
    <img src={props.img_src} alt="Project Logo" />
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
        <Logo img_src={imgUrl("dotty-logo.svg")} />
        <div className="inner">
          <ProjectTitle />
          <PromoSection>
            <Button href="https://docs.scala-lang.org/scala3/guides/migration/compatibility-intro.html">
              Go to docs.scala-lang.org
            </Button>
          </PromoSection>
        </div>
      </SplashContainer>
    );
  }
}

const Deprecation = () => (
  <Block background="light">
    {[
      {
        content:
          'The Scala 3 migration guide has moved to [docs.scala-lang.org](https://docs.scala-lang.org/scala3/guides/migration/compatibility-intro.html). This website is deprecated.',
        title: 'Deprecation'
      }
    ]}
  </Block>
)

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
        <div className = "mainContainer">
          <Deprecation />
        </div>
      </div>
    );
  }
}

module.exports = Index;
