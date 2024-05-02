import request from '@/utils/request'

// 查询用户资料相册列表
export function listUserPhoto(query) {
  return request({
    url: '/system/userPhoto/list',
    method: 'get',
    params: query
  })
}

// 查询用户资料相册详细
export function getUserPhoto(photoId) {
  return request({
    url: '/system/userPhoto/' + photoId,
    method: 'get'
  })
}

// 新增用户资料相册
export function addUserPhoto(data) {
  return request({
    url: '/system/userPhoto',
    method: 'post',
    data: data
  })
}

// 修改用户资料相册
export function updateUserPhoto(data) {
  return request({
    url: '/system/userPhoto',
    method: 'put',
    data: data
  })
}

// 删除用户资料相册
export function delUserPhoto(photoId) {
  return request({
    url: '/system/userPhoto/' + photoId,
    method: 'delete'
  })
}
