<template>
	<Card title="活动时间" class="ac-time">
		<view class="info" @click="selectTime">
			<view class="date u-flex">
				<view class="year input">
					{{year}}
				</view>
				年
				<view class="month input">
					{{month}}
				</view>
				月
				<view class="day input">
					{{day}}
				</view>
				日
			</view>
			<view class="select-time u-flex">
				<view class="input u-flex">
					{{currentTime}}
				</view>
				<text>{{amOrPm}}</text>
			</view>
		</view>
		<u-picker mode="time" v-model="showTime" :params="params" @confirm="confirm"></u-picker>
		<slot></slot>
	</Card>
</template>

<script>
	import Card from "@/components/card/card.vue"
	import dayjs from "../../common/dayjs/day.js"
	export default {
		components: {
			Card
		},
		props: {
			time: String,
		},
		watch: {
			currentTime(val) {
				const {
					year,
					month,
					day
				} = this;
				this.$emit('getTime', `${year}-${month}-${day} ${val}:00`)
			},
			time(val) {
				this.year = dayjs(val).year()
				this.month = dayjs(val).month() + 1
				this.day = dayjs(val).date()
				const hour = dayjs(val).hour()
				const minute = dayjs(val).minute()
				this.confirm({
					year: dayjs(val).year(),
					month: dayjs(val).month() + 1,
					day: dayjs(val).date(),
					hour,
					minute: minute ? minute : "00"
				})
			}
		},
		data() {
			return {
				showTime: false,
				year: "",
				month: "",
				day: "",
				params: {
					year: true,
					month: true,
					day: true,
					hour: true,
					minute: true,
					second: false
				},
				currentTime: "选择时间",
				amOrPm: 'AM'
			}
		},
		methods: {
			confirm(value) {
				const {
					year,
					month,
					day,
					hour,
					minute
				} = value
				this.year = year
				this.month = month
				this.day = day
				this.currentTime = `${hour}:${minute}`
				this.amOrPm = hour > 12 ? 'PM' : 'AM'
			},
			selectTime() {
				this.showTime = true
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>