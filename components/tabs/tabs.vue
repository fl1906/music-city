<template>
	<view class="tabs u-flex">
		<view v-for="(item,index) in list" :key="index" :class="{'tab-item':true,active:value==index}"
			@click="click(item,index)">
			{{item.name}}
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			current: {
				type: Number,
				default: 0
			},
			list: Array
		},
		data() {
			return {
				value: this.current
			}
		},
		methods: {
			click(item, index) {
				this.value = index
				this.$emit('change', {
					name: item.name,
					index
				})
			}
		}
	}
</script>

<style scoped>
	@import url('index.css');
</style>