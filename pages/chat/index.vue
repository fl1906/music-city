<template>
	<view class="chat">
		<NavBar :isBack="false" title="消息" />
		<u-tabs active-color="#FFF533" inactive-color="#FFF" bg-color="#150C24" font-size="30" :list="list"
			:is-scroll="false" :current="current" @change="change"></u-tabs>
		<view class="msgs u-flex" v-if="current ===0">
			<view class="item u-flex" v-for="(item,index) in msgList" :key="item.talkId" @click="toMsgPage(item)">
				<view class="info u-flex">
					<image :src="item.avatar" class="avatar"></image>
					<view class="name-msg">
						<view class="name">{{item.username}}</view>
						<view class="msg">{{item.message}}</view>
					</view>
				</view>
				<view class="count-time u-flex">
					<view class="time">{{timeForm(item.updateTime)}}</view>
					<view class="count u-flex" v-if="item.notReadCount">{{item.notReadCount}}</view>
					<view v-else></view>
				</view>
			</view>
		</view>
		<view class="admin u-flex" v-else>
			<view class="item" v-for="(item,index) in Array.from(Array(10))">
				<view class="time">07-02</view>
				<view class="content">
					<view class="title u-flex">
						<image src="../../static/line-yellow.png"></image>
						来自<text>奈斯</text>与您的组队信息：
					</view>
					<view class="tip">
						对方已到达目的地，您需要XX时间到达目的地，否则按照违约处理；或联系对方，申请无限制确认到达。
					</view>
					<view class="wh u-flex">
						<image src="../../static/admin-avatar.png" class="admin-avatar"></image>
						派之城官方提醒
					</view>
					<image src="../../static/split-line.png" class="split-line"></image>
					<view class="btns u-flex">
						<text class="contact">联系对方</text>
						<text class="confirm">确认信息</text>
					</view>
				</view>
			</view>
		</view>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Api from "../../api/index.js"
	import dayjs from "@/common/dayjs/day.js"
	export default {
		components: {
			NavBar
		},
		data() {
			return {
				list: [{
					name: '聊天消息'
				}, {
					name: '官方消息'
				}],
				current: 0,
				msgList: []
			}
		},
		methods: {
			change(index) {
				this.current = index;
			},
			// 前往消息页面
			toMsgPage(row) {
				const {
					toUserId
				} = row
				Api.PostTalkRead(toUserId)
				uni.navigateTo({
					url: `/pages/chat/msg/index?toUserId=${toUserId}`
				})
			},
			// 消息列表
			getTalkMylist() {
				Api.getTalkMylist((res) => {
					const data = res.data.rows
					this.msgList = data
				})
			},
			// 多久以前
			timeForm(time) {
				const timeBefore = dayjs(time).valueOf()
				const timeNow = dayjs().subtract(4, 'day').valueOf()
				return this.$u.timeFrom(timeBefore, timeBefore <= timeNow)
			}
		},
		onLoad() {
			this.getTalkMylist()
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>