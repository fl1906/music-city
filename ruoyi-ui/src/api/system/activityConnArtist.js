import request from '@/utils/request'

// 查询活动关联艺人列表
export function listActivityConnArtist(query) {
  return request({
    url: '/system/activityConnArtist/list',
    method: 'get',
    params: query
  })
}

// 查询活动关联艺人详细
export function getActivityConnArtist(activityConnArtistId) {
  return request({
    url: '/system/activityConnArtist/' + activityConnArtistId,
    method: 'get'
  })
}

// 新增活动关联艺人
export function addActivityConnArtist(data) {
  return request({
    url: '/system/activityConnArtist',
    method: 'post',
    data: data
  })
}

// 修改活动关联艺人
export function updateActivityConnArtist(data) {
  return request({
    url: '/system/activityConnArtist',
    method: 'put',
    data: data
  })
}

// 删除活动关联艺人
export function delActivityConnArtist(activityConnArtistId) {
  return request({
    url: '/system/activityConnArtist/' + activityConnArtistId,
    method: 'delete'
  })
}
