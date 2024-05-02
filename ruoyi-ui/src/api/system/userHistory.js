import request from '@/utils/request'

// 查询用户操作历史记录列表
export function listUserHistory(query) {
  return request({
    url: '/system/userHistory/list',
    method: 'get',
    params: query
  })
}

// 查询用户操作历史记录详细
export function getUserHistory(historyId) {
  return request({
    url: '/system/userHistory/' + historyId,
    method: 'get'
  })
}

// 新增用户操作历史记录
export function addUserHistory(data) {
  return request({
    url: '/system/userHistory',
    method: 'post',
    data: data
  })
}

// 修改用户操作历史记录
export function updateUserHistory(data) {
  return request({
    url: '/system/userHistory',
    method: 'put',
    data: data
  })
}

// 删除用户操作历史记录
export function delUserHistory(historyId) {
  return request({
    url: '/system/userHistory/' + historyId,
    method: 'delete'
  })
}
