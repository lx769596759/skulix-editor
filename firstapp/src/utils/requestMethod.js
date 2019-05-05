
// 后端请求地址
let serverPath = 'http://localhost:8083/'

export const post = (url, body) => {
  return new Promise((resolve, reject) => {
    mpvue.showLoading({
      title: '加载中...',
    })
    mpvue.request({
      url: serverPath + url,    // 拼接完整的 url
      data: body,
      method: 'POST',
      header: {
        'content-type': 'application/json;charset=UTF-8'
      },
      success(res) {
        if (res.statusCode != 200) {
          gk_requestStatus(res.statusCode);
          reject(res)
        }
        resolve(res.data)  // 把返回的数据传出去
      },
      fail(error) {
        gk_showToastNoIcon("请求超时")
        reject(error.data)   // 把错误信息传出去
      },
      complete: function () {
        mpvue.hideLoading()  // 关闭加载框
      }
    })
  })
}

export const get = (url, body) => {
  return new Promise((resolve, reject) => {
    mpvue.request({
      url: serverPath + url,    // 拼接完整的 url
      data: body,
      method: 'GET',
      success(res) {
        if (res.statusCode != 200) {
          gk_requestStatus(res.statusCode);
          reject(res)
        }
        resolve(res.data)  // 把返回的数据传出去
      },
      fail(error) {
        gk_showToastNoIcon("请求超时")
        reject(error.data)   // 把错误信息传出去
      },
      complete: function () {
        mpvue.hideLoading()  // 关闭加载框
      }
    })
  })
}

/**
* 统一处理一些请求错误（根据statusCode 来判断，而非后台返回的自定义code）
* 目前的statusCode 有
* 404() 500()
*/
const gk_requestStatus = (statusCode) => {
  let title = ''
  switch (statusCode) {
  case  404:
      title = '404 not found,请联系管理员！'
  break;
  case  500:
      title = '500 服务器内部错误,请稍后重试！'
  break;
  default:
      // 当做未知错误处理
      title = '未知错误,请稍后重试！'
  break;
  }
  gk_showToastNoIcon(title)
}

const gk_showToastNoIcon = (str) => {
  mpvue.showToast({
      title: str,
      icon: 'none',
      duration: 2500
  })
}