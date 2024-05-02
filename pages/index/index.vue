<template>
	<view>
		<view class="content">
			<!-- 页面背景图 -->
			<image src="../../static/bg.png" class="bg-img" />
			<!-- 导航栏 -->
			<NavBar>
				<view class="wrap">
					派之城
				</view>
			</NavBar>
			<!-- Search bar -->
			<view class="search-bar u-flex">
				<Search @change="getSearchValue" placeholder="搜索你想要参加的活动" />
				<image src="../../static/message-icon.png" class="message-icon" @click="toChatPage"></image>
				<view class="no-read" v-if="notReadCount"></view>
			</view>
			<template v-if="!searchValue">
				<!-- 顶部banner -->
				<u-swiper class="top-banner" @click="toActivityDetail" bg-color="transparent" height="408"
					:list="list"></u-swiper>
				<!-- 分类导航 -->
				<view class="cate-bar">
					<view class="music-bar" @click="toMusicPage">
						<image src="../../static/music-bg.png"></image>
						<view class="bar-context">
							<image src="../../static/music-text.png" class="music-text-img"></image>
							<view class="bar-content-title">汇聚全球热门电 子音乐节</view>
							<image src="../../static/brand.png" class="music-text-brand"></image>
						</view>
					</view>
					<view class="party-bar" @click="toPartyPage">
						<image src="../../static/party-bg.png"></image>
						<view class="bar-context">
							<image src="../../static/party-text.png" class="party-text-img"></image>
							<view class="bar-content-title">今天去哪玩？</view>
							<image src="../../static/party-brand.png" class="party-text-brand"></image>
						</view>
					</view>
				</view>
				<!-- 小导航 -->
				<view class="mini-bar">
					<view class="shop-bar">
						<image src="../../static/shop-bar-bg.png" class="bar-bg"></image>
						<view class="minbar-content">
							<view class="title">周边商城</view>
							<view class="text">敬请期待</view>
						</view>
						<image src="../../static/shop-bar.png" class="shop-bar-img"></image>
					</view>
					<view class="special-bar" @click="open">
						<image src="../../static/special-bar-bg.png" class="bar-bg"></image>
						<view class="minbar-content">
							<view class="title">特别计划</view>
							<view class="text">VRPAI-World</view>
						</view>
						<image src="../../static/special-bar.png" class="special-bar-img"></image>
					</view>
				</view>
			</template>
			<template v-else>
				
			</template>
			<Popup :show="moreTimeShow" @close="confirmToMoreTime(0)" @confirm="confirmToMoreTime(1)" cancelText="拒绝">
				<cover-view>对方想要超限制确认</cover-view>
				<cover-view>已与本人见面</cover-view>
			</Popup>
		</view>
		<u-loading :show="showLoading"></u-loading>
		<Tabbar activeKey="index" />
	</view>
</template>

<script>
	import Tabbar from "@/components/tabbar/tabbar.vue"
	import NavBar from "@/components/navbar/navbar.vue"
	import Search from "@/components/search/search.vue"
	import Api from "@/api/index.js"
	export default {
		components: {
			Tabbar,
			NavBar,
			Search
		},
		watch: {
			searchValue() {
				this.getListActivity()
			}
		},
		data() {
			return {
				imgList: [],
				list: [],
				showLoading: true,
				notReadCount: 0,
				searchValue: null,
				searchList: []
			}
		},
		methods: {
			// 获取搜索值
			getSearchValue(v) {
				this.searchValue = v
			},
			// 获取未读消息红点状态
			getNotRead() {
				Api.getNotRead((res) => {
					const i = res.data.data
					this.notReadCount = i
				})
			},
			open() {},
			// 轮播图列表
			getViewPageList() {
				Api.getViewPageList((res) => {
					this.imgList = res.data.rows
					this.list = res.data.rows.map((res) => ({
						image: res.imageUrl
					}))
					this.showLoading = false
				})
			},
			toMusicPage() {
				uni.navigateTo({
					url: '/pages/music/index'
				})
			},
			toPartyPage() {
				uni.navigateTo({
					url: '/pages/party/index'
				})
			},
			// 前往聊天页面
			toChatPage() {
				uni.navigateTo({
					url: "/pages/chat/index"
				})
			},
			// 前往活动页
			toActivityDetail(index) {
				const item = this.list[index]
				if (!item.activityId) return
				uni.navigateTo({
					url: "/pages/activityDetail/index?activityId=" + item.activityId
				})
			},
			// 获取活动列表
			getListActivity() {
				Api.getListActivity({
					title: this.searchValue
				}, (res) => {
					this.searchList = res.data.rows
				})
			}
		},
		onLoad() {
			this.getViewPageList()
			this.getNotRead()
		},
	}
</script>

<style scoped>
	@import url('index.css');
</style>