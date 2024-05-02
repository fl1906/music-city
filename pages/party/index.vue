<template>
	<view class="party">
		<NavBar :isBack="false" title="派对" />
		<Search placeholder="搜索你想要参加的派对" class="search"></Search>
		<u-loading :show="showLoading"></u-loading>
		<template v-if="!showLoading">
			<Dropdown title="活动地区">
				<view class="select-bar u-flex">
					<view class="province u-flex">
						<view v-for="item in provinceList" :key="item" :class="{item:true,active:activeProvince==item}"
							@click="selectRegion(item)">
							{{item}}
						</view>
					</view>
					<view class="city u-flex">
						<view v-for="(item,index) in regionList[activeProvince]" :key="item.regionId"
							:class="{item:true,active:index==activeCity}" @click="setRadio(index)">
							{{item.name}}
						</view>
					</view>
					<view class="radio">
						<u-radio-group v-model="activeCity">
							<view class="u-flex">
								<u-radio active-color="#FFF533" :name="index"
									v-for="(item,index) in regionList[activeProvince]" :key="item.regionId"
									@change="setRadio">
								</u-radio>
							</view>
						</u-radio-group>
					</view>
				</view>
			</Dropdown>
			<view class="parties u-flex">
				<view class="item u-flex">
					<image src="../../static/region-bg.png" class="region-bg"></image>
					<view class="region-name">{{currentCity.name}}</view>
					<view class="text u-flex">
						<image src="../../static/lv1tag-left.png" mode=""></image>
						地区活动
						<image src="../../static/lv1tag-right.png" mode=""></image>
					</view>
				</view>
				<view class="item" v-for="item in acList" :key="item.activityId" @click="toDetailPage(item.activityId)">
					<image src="../../static/region-item-bg.png" class="region-bg-item"></image>
					<view class="party-name">{{item.title}}</view>
					<view class="date">
						2023.05.30 - 06.02
					</view>
					<image :src="item.coverImage" class="cover" mode="aspectFill"></image>
				</view>
			</view>
		</template>
		<Popup :show="moreTimeShow" @clickMaskHide="closeMoreTime" @close="confirmToMoreTime(0)"
			@confirm="confirmToMoreTime(1)" cancelText="拒绝">
			<cover-view>对方想要超限制确认</cover-view>
			<cover-view>已与本人见面</cover-view>
		</Popup>
	</view>
</template>

<script>
	import NavBar from "@/components/navbar/navbar.vue"
	import Search from "@/components/search/search.vue"
	import Api from '@/api/index.js'
	import Dropdown from "@/components/dropdown/dropdown.vue"
	export default {
		components: {
			NavBar,
			Search,
			Dropdown
		},
		watch: {
			currentCity() {
				const regionId = this.currentCity.regionId
				this.getListActivity(regionId)
			}
		},
		data() {
			return {
				showLoading: true,
				acList: [],
				activeProvince: '全部',
				activeCity: 0,
				open: false,
				provinceList: [],
				regionList: [],
				currentCity: null,
			}
		},
		methods: {
			// 前往活动详情页
			toDetailPage(id) {
				uni.navigateTo({
					url: "/pages/activityDetail/index?activityId=" + id
				})
			},
			// 获取活动列表
			getListActivity(regionId) {
				Api.getListActivity({
					pageNum: 1,
					pageSize: 10,
					classify: 1,
					regionId
				}, (res) => {
					const data = res.data.rows
					this.acList = data
				})
			},
			setRadio(i) {
				this.activeCity = i;
				this.currentCity = this.regionList[this.activeProvince][i]
			},
			selectRegion(name) {
				this.activeProvince = name
				this.activeCity = 0
				this.currentCity = this.regionList[name][0]
			},
			// 获得地区列表
			getRegionList() {
				let _this = this;
				Api.getRegionList((res) => {
					const data = res.data.data
					const provinceList = Object.keys(data).sort((a, b) => a.length - b.length)
					this.provinceList = provinceList
					this.regionList = data

					const Key = "UU6BZ-44QWT-XCMXL-V2M6A-DYU6V-B3FRF"
					uni.getLocation({
						type: 'wgs84',
						success: function(res) {
							const longitude = res.longitude;
							const latitude = res.latitude;
							uni.request({
								url: `https://apis.map.qq.com/ws/geocoder/v1/`,
								data: {
									key: Key,
									location: `${latitude},${longitude}`
								},
								success: (res) => {
									const {
										city,
										province
									} = res.data.result.address_component
									let regionId = data[province].find((item, index) => {
										_this.activeCity = index
										_this.currentCity = item
										return city.startsWith(item.name)
									}).regionId;
									if (!regionId) {
										regionId = data[de][0].regionId
										_this.currentCity = data[de][0]
									}
									_this.activeProvince = province
									_this.showLoading = false
								}
							})
						}
					});
				})
			}
		},
		onLoad() {
			this.getRegionList()
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>