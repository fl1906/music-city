<template>
	<view class="item u-flex" @click="$emit('click',{groupId:item.groupId,activityId:item.activityId})">
		<image src="../../static/team-bg.png" class="team-bg"></image>
		<view class="info u-flex">
			<image class="cover" :src="item.user.avatar" mode="aspectFill">
			</image>
			<image src="../../static/female.png" class="gender" v-if="item.user.sex==1"></image>
			<image src="../../static/male.png" class="gender" v-else></image>
			<view class="content">
				<view class="title">{{item.title}}</view>
				<view class="cate u-flex">
					<view class="level">{{item.user.userLevel}}级</view>
					<image src="../../static/split-line-vertical.png" class="split-line-vertical"></image>
					<view class="pay-way">{{returnGroupTypeLabel(item.groupType)}}</view>
					<image src="../../static/split-line-vertical.png" class="split-line-vertical"></image>
					<view class="paibi u-flex">
						<image src="../../static/paibi.png" mode=""></image>
						X
						{{item.money}}
					</view>
				</view>
				<view class="local u-flex">
					<u-icon name="map" color="#A7A7A7" class="map-icon"></u-icon>
					<view class="address">
						<view>{{item.address}}</view>
					</view>
				</view>
			</view>
			<view class="time">
				<view class="box u-flex">
					<view class="month">{{getMonth(item.activityTime)}}</view>
					<view class="line"></view>
					<view class="day">{{getDay(item.activityTime)}}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		mapState
	} from "vuex"
	import dayjs from "../../common/dayjs/day.js"
	import utils from "@/utils/index.js"
	export default {
		emits: ['to-page'],
		props: {
			item: Object
		},
		computed: {
			...mapState(['userInfo'])
		},
		methods: {
			getMonth(time) {
				return dayjs(time).format('MM')
			},
			getDay(time) {
				return dayjs(time).format('DD')
			},
			returnGroupTypeLabel(val){
				return utils.returnGroupTypeLabel(val)
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>