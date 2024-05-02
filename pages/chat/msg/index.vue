<template>
	<view class="msg">
		<NavBar :isBack="false" title="聊天" />
		<view class="top">
			<view class="info u-flex">
				<image :src="toUserInfo.avatar" class="avatar">
				</image>
				<view class="user-info">
					<view class="name">{{toUserInfo.nickName}}</view>
					<view class="private u-flex">
						<view class="level item  u-flex">Lv.3</view>
						<view class="age item u-flex">
							<image src="../../../static/male-chat.png"></image>
							<text>24</text>
						</view>
						<view class="position item u-flex">{{toUserInfo.address}}</view>
					</view>
					<view class="desc">{{toUserInfo.info}}</view>
				</view>
			</view>
			<view class="photos u-flex">
				<view class="title">Ta的相册：</view>
				<view class="items u-flex">
					<view class="item" v-for="(item,index) in toUserInfo.photo" :key="index" @click="preview(index)">
						<image :src="item"></image>
					</view>
				</view>
			</view>
		</view>
		<scroll-view class="chat-main" scroll-y="true" :scroll-into-view="scrollToView"
			:style="{height:(showEmoji||showMore)?'436rpx':height}">
			<view class="success u-flex">
				成功组队！赶紧发个消息吧~
			</view>
			<view class="chat-item" v-for="(item,index) in textArr" :key="index" :id="'msg' + index">
				<view class="time" v-if="showTime(item,index)">{{item.createTime}}</view>
				<!-- b - 对方的消息  -->
				<view class="content-wrapper-left u-flex" v-if="item.fromUserId !== userInfo.userId">
					<image :src="toUserInfo.avatar" class="avatar avatar-left"></image>
					<view class="chat-content-left u-flex">{{item.message}}</view>
				</view>
				<!--a - 自己的信息-->
				<view class="content-wrapper-right u-flex" v-if="item.fromUserId === userInfo.userId">
					<view class="chat-content-right u-flex">{{item.message}}</view>
					<image :src="userInfo.avatar" class="avatar avatar-right"></image>
				</view>
			</view>
		</scroll-view>
		<view class="fix-bottom">
			<view class="bar u-flex">
				<input v-model="value" confirm-type="search" @confirm="add(value)" />
				<image src="../../../static/emo.png" class="emo" @click="handeShowEmoji"></image>
				<image src="../../../static/add-icon.png" class="add-icon" @click="handChangeMore"></image>
			</view>
			<view class="btns u-flex" v-if="showMore">
				<view class="item">
					<image src="../../../static/photo-chat.png"></image>
					<view>照片</view>
				</view>
				<view class="item">
					<image src="../../../static/photograph-chat.png"></image>
					<view>拍摄</view>
				</view>
				<view class="item">
					<image src="../../../static/position-chat.png"></image>
					<view>定位</view>
				</view>
			</view>
			<view class="emoji u-flex" v-if="showEmoji">
				<view v-for="(item,index) in emojis" :key="index" @click="putEmoji(item)">{{item}}</view>
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
	import Api from "@/api/index.js"
	import dayjs from "@/common/dayjs/day.js"
	import emoji from "@/common/lib/emoji.js"

	import {
		mapState,
		mapActions
	} from "vuex"
	export default {
		components: {
			NavBar
		},
		computed: {
			...mapState({
				userInfo: state => state.userInfo,
				messing: state => state.messing,
			}),
		},
		watch: {
			messing() {
				if (!this.messing) return
				this.getUserTalk()
			}
		},
		data() {
			return {
				value: "",
				textArr: [],
				emojis: [],
				scrollToView: "",
				height: '630rpx',
				showMore: false,
				showEmoji: false,
				toUserId: null,
				toUserInfo: null
			}
		},
		methods: {
			...mapActions(['sendMsg']),
			putEmoji(item) {
				this.value = this.value + item
			},
			async add(text) {
				if (!text) return
				this.$nextTick(function() {
					this.scrollToIndex()
				});
				await this.sendMsg({
					message: text,
					fromUserId: this.userInfo.userId,
					toUserId: this.toUserId
				})

				this.getUserTalk()
			},
			handChangeMore() {
				this.showMore = !this.showMore
				this.showEmoji = false
				this.scrollToView = ""
				this.$nextTick(function() {
					const index = this.textArr.length - 1;
					this.scrollToView = 'msg' + index;
				});
			},
			handeShowEmoji() {
				this.showEmoji = !this.showEmoji
				this.showMore = false
				this.scrollToView = ""
				this.$nextTick(function() {
					const index = this.textArr.length - 1;
					this.scrollToView = 'msg' + index;
				});
			},
			scrollToIndex() {
				this.$nextTick(function() {
					const index = this.textArr.length - 1;
					this.scrollToView = 'msg' + index;
				});
			},
			getUserTalk() {
				Api.getUserTalk({
					toUserId: this.toUserId
				}, (res) => {
					const data = res.data.rows
					const sort = (a, b) => new Date(a.createTime).getTime() - new Date(b.createTime).getTime()
					this.textArr = []
					this.textArr.push(...data.sort(sort))
				})
			},
			getTalkUserLine(userId) {
				Api.getTalkUserLine({
					userId
				}, (res) => {
					const data = res.data.data
					this.toUserInfo = data
				})
			},
			showTime(item, index) {
				if (index == this.textArr.length - 1) return
				if (index === 0) return true
				const timeNow = dayjs(item.createTime).minute()
				const timeBefore = dayjs(this.textArr[index - 1].createTime).minute()
				return (timeNow - timeBefore) > 1
			},
			// 图片预览
			preview(index) {
				uni.previewImage({
					urls: this.toUserInfo.photo,
					current: this.toUserInfo.photo[index]
				})
			}
		},
		onLoad(res) {
			const toUserId = res.toUserId
			this.toUserId = toUserId;
			this.getUserTalk()
			this.getTalkUserLine(toUserId)
			this.scrollToIndex()
			this.emojis = emoji
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>