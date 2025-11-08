import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
    lang: "en-US",
    title: "NoctisUI",
    description: "A powerful UI library for Minecraft Fabric mods",
    sitemap: {
        hostname: 'https://noctisui.libnaus.fr',
    },
    head: [
        ['link', { rel: 'icon', href: '/favicon.ico' }]
    ],
    themeConfig: {
        // https://vitepress.dev/reference/default-theme-config
        logo: '/logo.svg',
        nav: [
            { text: 'Home', link: '/' },
            { text: 'Getting Started', link: '/getting-started' },
            { text: 'API Reference', link: '/api/components' },
            { text: 'Examples', link: '/examples/basic-button' }
        ],
        sidebar: [
            {
                text: 'Introduction',
                items: [
                    { text: 'Getting Started', link: '/getting-started' }
                ]
            },
            {
                text: 'API Reference',
                items: [
                    { text: 'Components', link: '/api/components' }
                ]
            },
            {
                text: 'Examples',
                items: [
                    { text: 'Basic Button', link: '/examples/basic-button' },
                    { text: 'Container Layout', link: '/examples/container-layout' },
                    { text: 'Custom Screen', link: '/examples/custom-screen' }
                ]
            },
            {
                text: 'Guides',
                items: [
                    { text: 'Styling Guide', link: '/guides/styling' }
                ]
            }
        ],
        socialLinks: [
            { icon: 'github', link: 'https://github.com/AxenoDev/NoctisUI' }
        ],
        search: {
            provider: 'local'
        },
        footer: {
            message: 'Released under the GPL-3.0 License',
            copyright: 'Copyright © 2024-2025 Libnaus'
        }
    }
})
