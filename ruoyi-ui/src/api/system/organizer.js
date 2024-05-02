import request from '@/utils/request'

// 查询活动主办方列表
export function listOrganizer(query) {
  return request({
    url: '/system/organizer/list',
    method: 'get',
    params: query
  })
}

// 查询活动主办方详细
export function getOrganizer(organizerId) {
  return request({
    url: '/system/organizer/' + organizerId,
    method: 'get'
  })
}

// 新增活动主办方
export function addOrganizer(data) {
  return request({
    url: '/system/organizer',
    method: 'post',
    data: data
  })
}

// 修改活动主办方
export function updateOrganizer(data) {
  return request({
    url: '/system/organizer',
    method: 'put',
    data: data
  })
}

// 删除活动主办方
export function delOrganizer(organizerId) {
  return request({
    url: '/system/organizer/' + organizerId,
    method: 'delete'
  })
}
