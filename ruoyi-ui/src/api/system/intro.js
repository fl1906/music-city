import request from '@/utils/request'

// 查询活动介绍列表
export function listIntro(query) {
  return request({
    url: '/system/intro/list',
    method: 'get',
    params: query
  })
}

// 查询活动介绍详细
export function getIntro(introId) {
  return request({
    url: '/system/intro/' + introId,
    method: 'get'
  })
}

// 新增活动介绍
export function addIntro(data) {
  return request({
    url: '/system/intro',
    method: 'post',
    data: data
  })
}

// 修改活动介绍
export function updateIntro(data) {
  return request({
    url: '/system/intro',
    method: 'put',
    data: data
  })
}

// 删除活动介绍
export function delIntro(introId) {
  return request({
    url: '/system/intro/' + introId,
    method: 'delete'
  })
}
