module.exports = {
  chainWebpack(config) {
    config.module
      .rule('vue')
      .use('vue-loader')
      .loader('vue-loader')
      .tap((options) => {
        const compile = options.compiler.compile
        options.compiler.compile = (template, info) => {
          // 获取所有页面（排除所有组件），插入<NoticeBar />组件
          if (
            info.resourcePath?.match(/^pages/) &&
            !info.resourcePath?.match(/\/components\//)
          ) {
            template = template.trim()
            template = template.replace(
              /^<[\d\D]+?>/g,
              (_) => `${_}<MusicHover />
              `
            )
          }
          return compile(template, info)
        }
        return options
      })
  }
}