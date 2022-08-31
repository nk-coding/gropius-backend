// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const lightCodeTheme = require('prism-react-renderer/themes/github');
const darkCodeTheme = require('prism-react-renderer/themes/dracula');

const apiSidebar = require('./sidebars').apiSidebar
const graphqlSidebar = require('./sidebars').graphqlSidebar

/** @type {import('@docusaurus/types').Config} */
const config = {
    title: 'Gropius Backend',
    url: 'https://ccims.github.io/',
    baseUrl: '/gropius-backend-docs/',
    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'throw',
    onDuplicateRoutes: 'throw',
    organizationName: 'ccims',
    projectName: 'gropius-backend-docs',
    trailingSlash: false,

    presets: [
        [
            '@docusaurus/preset-classic',
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                docs: {
                    sidebarPath: require.resolve('./sidebars.js'),
                    routeBasePath: '/',
                },
                blog: false,
                theme: {
                    customCss: [require.resolve('./src/css/custom.css')],
                },
            }),
        ],
    ],

    themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
        ({
        colorMode: {
            defaultMode: 'dark',
        },
        navbar: {
            title: 'Gropius Backend',
            items: [{
                    type: 'doc',
                    docId: 'docs/docs',
                    position: 'left',
                    label: 'Docs',
                },
                {
                    type: 'doc',
                    docId: apiSidebar[0]?.items[0]?.id ?? apiSidebar[0]?.items[0]?.link?.id ?? "docs/docs",
                    position: 'left',
                    label: 'API',
                },
                {
                    type: 'doc',
                    docId: 'graphql/api-public',
                    position: 'left',
                    label: "GraphQL"
                },
                {
                    href: 'https://github.com/ccims/gropius-backend',
                    label: 'GitHub',
                    position: 'right',
                },
            ],
        },
        footer: {
            style: 'dark',
            copyright: `Built with Docusaurus.`,
        },
        prism: {
            theme: lightCodeTheme,
            darkTheme: darkCodeTheme,
            defaultLanguage: 'kotlin',
            additionalLanguages: ['kotlin'],
        },
    }),

    plugins: [
        () => ({
            name: 'custom-webpack-loaders',
            configureWebpack: () => ({
                module: {
                    rules: [
                        {
                            test: /\.source$/,
                            type: 'asset/source'
                        }
                    ]
                }
            })
        }),
        [
            "@edno/docusaurus2-graphql-doc-generator",
            {
                id: "api-public",
                schema: "./schemas/api-public.gql",
                rootPath: "./docs",
                baseURL: "graphql/api-public",
                docOptions: {
                    index: true
                }
            },
        ],
    ]
};

module.exports = config;