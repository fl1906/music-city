<template>
	<view class="music">
		<NavBar :isBack="false" title="电音节" />
		<Search class="search" placeholder="搜索你想要参加的电音节" />
		<u-tabs active-color="#FFF533" inactive-color="#FFF" bg-color="#150C24" :show-bar="false" font-size="32"
			:list="list" :is-scroll="false" :current="current" @change="change" class="tab"></u-tabs>
		<ScrollList @scrollBottom="scrollBottom" height="80vh" class="items">
			<view class="item" v-for="(item,index) in acList" :key="item.activityId"
				@click="toDetailPage(item.activityId)">
				<image src="../../static/music-bg-black.png" class="music-bg-black"></image>
				<view class="title">{{item.title}}</view>
				<view class="time u-flex">
					<view class="u-flex">
						<text class="date">{{getDate(item.startTime)}}</text>
						<text class="day">{{getDay(item.startTime)}}</text>
					</view>
					<text class="line">—</text>
					<view class="u-flex">
						<text class="date">{{getDate(item.endDate)}}</text>
						<text class="day">{{getDay(item.endDate)}}</text>
					</view>
				</view>
				<image class="type" src="../../static/country.png" v-if="item.region===0"></image>
				<image class="type" src="../../static/inland.png" v-else></image>
				<view class="cover">
					<image :src="item.coverImage" mode="aspectFill" />
				</view>
			</view>
		</ScrollList>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Search from "@/components/search/search.vue"
	import Api from '../../api/index.js'
	import dayjs from '../../common/dayjs/day.js'
	import ScrollList from "../../components/scroll-list/scroll-list.vue"

	export default {
		components: {
			NavBar,
			Search,
			ScrollList
		},
		data() {
			return {
				list: [{
					name: '全部'
				}, {
					name: '国内'
				}, {
					name: '国际',
				}],
				current: 0,
				acList: [],
				page: 1
			}
		},
		watch: {
			current() {
				this.getListActivity()
			},
			page() {
				this.getListActivity()
			}
		},
		methods: {
			scrollBottom() {
				this.page += 1
			},
			change(index) {
				this.current = index;
			},
			// 前往活动详情页
			toDetailPage(id) {
				uni.navigateTo({
					url: `/pages/activityDetail/index?activityId=${id}`
				})
			},
			// 返回年月
			getDate(time) {
				return dayjs(time).format('YYYY-MM-DD')
			},
			//星期几
			getDay(time) {
				const day = dayjs(time).day()
				switch (day) {
					case 0:
						return '周日'
					case 1:
						return '周一'
					case 2:
						return '周二'
					case 3:
						return '周三'
					case 4:
						return '周四'
					case 5:
						return '周五'
					case 6:
						return '周六'
				}
			},
			// 获取活动列表
			getListActivity() {
				Api.getListActivity({
					page: this.page,
					pageSize: 10,
					classify: 0,
					region: this.current - 1 >= 0 ? this.current - 1 : ""
				}, (res) => {
					const data = res.data.rows
					this.acList = data
				})
			}
		},
		onLoad() {
			this.getListActivity()
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>