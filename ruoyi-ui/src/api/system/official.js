import request from '@/utils/request'

// 查询官方消息列表
export function listOfficial(query) {
  return request({
    url: '/system/official/list',
    method: 'get',
    params: query
  })
}

// 查询官方消息详细
export function getOfficial(officialId) {
  return request({
    url: '/system/official/' + officialId,
    method: 'get'
  })
}

// 新增官方消息
export function addOfficial(data) {
  return request({
    url: '/system/official',
    method: 'post',
    data: data
  })
}

// 修改官方消息
export function updateOfficial(data) {
  return request({
    url: '/system/official',
    method: 'put',
    data: data
  })
}

// 删除官方消息
export function delOfficial(officialId) {
  return request({
    url: '/system/official/' + officialId,
    method: 'delete'
  })
}
