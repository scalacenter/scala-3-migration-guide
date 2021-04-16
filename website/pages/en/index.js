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
            <Button href={docUrl("get-started.html", language)}>
              Get started
            </Button>
          </PromoSection>
        </div>
      </SplashContainer>
    );
  }
}

const Compatibility = () => (
  <Block background="light">
    {[
      {
        content:
          'Scala 3 has been carefully designed to improve the backward and forward compatibility of the Scala programming language.\n\n' +
          `In the [Compatibility Reference](${docUrl('get-started.html')}) ` +
          'you will learn about the compatibility between Scala 2.13 and Scala 3 in the context of the migration.',
        image: `${imgUrl('puzzle-primary.svg')}`,
        imageAlt: 'Icon made by Nikita Kozin from the Noun Project',
        imageAlign: 'left',
        title: `[Compatibility](${docUrl('get-started.html')})`,
      },
    ]}
  </Block>
);

const MigrationMode = () => (
  <Block>
    {[
      {
        content:
          'The Scala 3 compiler is, in itself, a powerfull migration tool that can assist you during the transition from Scala 2.13 to Scala 3.\n\n ' +
          `Learn about the migration features of the tooling ecosystem in the [Migration Tools](${docUrl('tooling/migration-tools.html')}) section`,
        title: `[Migration Tools](${docUrl('tooling/migration-tools.html')})`,
        image: `${imgUrl('tools-primary.svg')}`,
        imageAlt: 'Icon made by Maxime Kulikov from the Noun Project.',
        imageAlign: 'right',
      },
    ]}
  </Block>
);

const Metaprogramming = () => (
  <Block background="light">
    {[
      {
        content:
          'Scala 3 provides a toolbox full of metaprogramming features, which are safer, more robust, and much more stable than their counterparts in Scala 2. ' +
          'Implementing macro libraries in Scala 3 is simpler and the resulting libraries are easier to maintain across future versions of Scala. ' +
          'The improvements come at a price: existing macro libraries need to be re-implemented.',
        title: `[Metaprogramming](${docUrl('compatibility/metaprogramming.html')})`,
        image: `${imgUrl('magnifying-primary.svg')}`,
        imageAlt: 'Icon made by Eucalyp from the Noun Project.',
        imageAlign: 'left',
      },
    ]}
  </Block>
);

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
          <Compatibility />
          <MigrationMode />
          <Metaprogramming />
        </div>
      </div>
    );
  }
}

module.exports = Index;
