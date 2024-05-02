import request from '@/utils/request'

// 查询主办方票务列表
export function listOrganizerTicket(query) {
  return request({
    url: '/system/organizerTicket/list',
    method: 'get',
    params: query
  })
}

// 查询主办方票务详细
export function getOrganizerTicket(organizerTicketId) {
  return request({
    url: '/system/organizerTicket/' + organizerTicketId,
    method: 'get'
  })
}

// 新增主办方票务
export function addOrganizerTicket(data) {
  return request({
    url: '/system/organizerTicket',
    method: 'post',
    data: data
  })
}

// 修改主办方票务
export function updateOrganizerTicket(data) {
  return request({
    url: '/system/organizerTicket',
    method: 'put',
    data: data
  })
}

// 删除主办方票务
export function delOrganizerTicket(organizerTicketId) {
  return request({
    url: '/system/organizerTicket/' + organizerTicketId,
    method: 'delete'
  })
}
