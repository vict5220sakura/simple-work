/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{html,ts,tsx,js,jsx,tsx,vue}"],
  theme: {
    extend: {
      fontSize:{
        testsss: ['8rem', {lineHeight: '1.2'}]
      },
      colors:{
        bootstrap_primary: '#0d6efd',
        bootstrap_secondary: '#6c757d',
        bootstrap_success: '#198754',
        bootstrap_danger: '#dc3545',
        bootstrap_info: '#0dcaf0',
      },
      fontFamily: {
        custom: ['MyFont'],
        mao: ['mao']
      }
    },
  },
  plugins: [],
  corePlugins: {
    preflight: true, /* 预检 */
  }
}

