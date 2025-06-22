import axios from 'axios'

 //导入路由实例
import router from "../router.js";
const baseUrl = "http://localhost:8443/eCommerce"
// 创建实例
const server = axios.create({
  timeout: 3000,
})
//请求拦截器
server.interceptors.request.use(config => {
  // 从localStorage获取token
  const token = sessionStorage.getItem('token');
  if (token) {
    config.headers.Authorization = token; // 将token添加到请求头中
  }
  return config;
}, error => {
  // 请求错误处理
  return Promise.reject(error);
})

// 响应拦截器
server.interceptors.response.use(response => {
  const authErrorCodes = [401, 402]
  if (authErrorCodes.includes(response.data.code)) {
    // 统一处理认证错误
    alert('签名失效，请重新登录')
    const loginPath = response.data.code === 401 ? '/admin/login' : '/login'
    router.push(loginPath)
  }
  return response
}, error => {
  // 响应错误处理
  return Promise.reject(error)
})

// 封装post请求
export function myPost(url, params) {
  return new Promise((resolve, reject) => {
    server.post(url, params)
      .then(res => {
        resolve(res)
      })
      .catch((err) => {
        reject(err)
      })
  })
}
// 封装get请求
export function myGet(url, myParams) {
  return new Promise((resolve, reject) => {
    server.get(url,  {params: myParams })
      .then(res => {
        resolve(res)
      })
      .catch((err) => {
        reject(err)
      })
  })
}
export default { // 导出实例
  baseUrl,
  myPost(url, params) {
    return myPost(url, params)
  },
  myGet(url, params) {
    return myGet(url, params)
  }
}