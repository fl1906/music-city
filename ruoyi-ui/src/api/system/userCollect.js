import request from '@/utils/request'

// 查询用户收藏活动列表
export function listUserCollect(query) {
  return request({
    url: '/system/userCollect/list',
    method: 'get',
    params: query
  })
}

// 查询用户收藏活动详细
export function getUserCollect(collectId) {
  return request({
    url: '/system/userCollect/' + collectId,
    method: 'get'
  })
}

// 新增用户收藏活动
export function addUserCollect(data) {
  return request({
    url: '/system/userCollect',
    method: 'post',
    data: data
  })
}

// 修改用户收藏活动
export function updateUserCollect(data) {
  return request({
    url: '/system/userCollect',
    method: 'put',
    data: data
  })
}

// 删除用户收藏活动
export function delUserCollect(collectId) {
  return request({
    url: '/system/userCollect/' + collectId,
    method: 'delete'
  })
}
