import request from '@/utils/request'

// 查询用户聊天列表
export function listUserTalk(query) {
  return request({
    url: '/system/userTalk/list',
    method: 'get',
    params: query
  })
}

// 查询用户聊天详细
export function getUserTalk(talkId) {
  return request({
    url: '/system/userTalk/' + talkId,
    method: 'get'
  })
}

// 新增用户聊天
export function addUserTalk(data) {
  return request({
    url: '/system/userTalk',
    method: 'post',
    data: data
  })
}

// 修改用户聊天
export function updateUserTalk(data) {
  return request({
    url: '/system/userTalk',
    method: 'put',
    data: data
  })
}

// 删除用户聊天
export function delUserTalk(talkId) {
  return request({
    url: '/system/userTalk/' + talkId,
    method: 'delete'
  })
}
