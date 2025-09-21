import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
    lang: "en-US",
    title: "NoctisUI - Libnaus",
    description: "NoctisUI is a Minecraft UI library",
    sitemap: {
        hostname: 'https://noctisui.libnaus.fr',
    },
    head: [
        ['link', { rel: 'icon', href: '/favicon.ico' }]
    ],
    themeConfig: {
        // https://vitepress.dev/reference/default-theme-config
        nav: [
            { text: 'Home', link: '/' },
            { text: 'Examples', link: '/markdown-examples' }
        ],
        sidebar: [
            {
                text: 'Examples',
                items: [
                    { text: 'Markdown Examples', link: '/markdown-examples' },
                    { text: 'Runtime API Examples', link: '/api-examples' }
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
            message: 'Copyright © 2024-2025 Libnaus',
        }
    }
})
