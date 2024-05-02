import request from '@/utils/request'

// 查询活动介绍与活动关联列表
export function listActivityConnIntro(query) {
  return request({
    url: '/system/activityConnIntro/list',
    method: 'get',
    params: query
  })
}

// 查询活动介绍与活动关联详细
export function getActivityConnIntro(activityConnIntroId) {
  return request({
    url: '/system/activityConnIntro/' + activityConnIntroId,
    method: 'get'
  })
}

// 新增活动介绍与活动关联
export function addActivityConnIntro(data) {
  return request({
    url: '/system/activityConnIntro',
    method: 'post',
    data: data
  })
}

// 修改活动介绍与活动关联
export function updateActivityConnIntro(data) {
  return request({
    url: '/system/activityConnIntro',
    method: 'put',
    data: data
  })
}

// 删除活动介绍与活动关联
export function delActivityConnIntro(activityConnIntroId) {
  return request({
    url: '/system/activityConnIntro/' + activityConnIntroId,
    method: 'delete'
  })
}
