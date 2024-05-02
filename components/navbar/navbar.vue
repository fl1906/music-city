<template>
	<u-navbar :is-back="isBack" :title="title" :background="background" :border-bottom="false" title-color="#FFF">
		<slot></slot>
	</u-navbar>
</template>

<script>
	export default {
		props: {
			isBack: Boolean,
			title: String
		},
		data() {
			return {
				background: {
					backgroundColor: 'transparent',
				}
			}
		},
		methods: {

		}
	}
</script>

<style>
</style>