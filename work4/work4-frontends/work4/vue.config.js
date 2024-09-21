const { defineConfig } = require('@vue/cli-service');

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端 API 地址
        changeOrigin: true, // 是否修改请求头中的 Origin
        pathRewrite: { '^/api': '' }, // 路径重写，将/api去掉
      },
    },
  },
});

