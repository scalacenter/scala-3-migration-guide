// See https://docusaurus.io/docs/site-config for all the possible
// site configuration options.

const repoUrl = "https://github.com/scalacenter/scala-3-migration-guide";

const siteConfig = {
  title: "Scala 3 Migration guide",
  tagline: "An evolving guide to support the migration to Scala 3",

  url: "https://scalacenter.github.io/",
  baseUrl: "/scala-3-migration-guide/",

  // Used for publishing and more
  projectName: "scala-3-migration-guide",
  organizationName: "scalacenter",

  customDocsPath: 'migration-docs/target/mdoc',

  // For no header links in the top nav bar -> headerLinks: [],
  headerLinks: [
  {doc: 'get-started', label: 'User Guide'},
  {doc: 'contributing', label: 'Contribute'},
  { href: repoUrl, label: "GitHub", external: true }
  ],

  /* path to images for header/footer */
  headerIcon: 'img/dotty-logo.svg',
  footerIcon: 'img/dotty-logo-white.svg',
  favicon: 'img/dotty-logo.svg',

  /* Colors for website */
  colors: {
    primaryColor: '#414551',
    secondaryColor: '#ca445e',
  },

  /* Custom fonts for website */
  /*
  fonts: {
    myFont: [
      "Times New Roman",
      "Serif"
    ],
    myOtherFont: [
      "-apple-system",
      "system-ui"
    ]
  },
  */

  // This copyright info is used in /core/Footer.js and blog rss/atom feeds.
  copyright: `Copyright © ${new Date().getFullYear()} Scala Center`,

  highlight: {
    // Highlight.js theme to use for syntax highlighting in code blocks
    theme: "github"
  },


  // Add custom scripts here that would be placed in <script> tags.
//  scripts: ['https://buttons.github.io/buttons.js'],

  // On page navigation for the current documentation page.
  onPageNav: 'separate',
  // No .html extensions for paths.

  /* Open Graph and Twitter card images */
  ogImage: "img/scalacenter2x.png",
  twitterImage: "img/scalacenter2x.png",

  editUrl: `${repoUrl}/edit/master/docs/`,

  repoUrl
};

module.exports = siteConfig;
