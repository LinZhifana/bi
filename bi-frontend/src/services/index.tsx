import { Api } from './Api'

const API = new Api<unknown>()

// 添加请求拦截器

API.instance.interceptors.request.use(
  (config) => {
    // 在这里可以添加全局配置，例如 withCredentials
    config.withCredentials = true
    // 可以在此处添加更多逻辑，例如添加授权头
    // config.headers['Authorization'] = `Bearer ${token}`;
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

export { API }
