import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
import uView from "uview-ui";
import store from './stroe/index.js'
Vue.use(uView);

import popup from "@/components/popup/popup.vue"
Vue.component("popup",popup)

import MusicHover from '@/components/music-hover/music-hover.vue'
Vue.component('MusicHover', MusicHover)

import Tabs from '@/components/tabs/tabs.vue'
Vue.component('Tabs', Tabs)

import pageLoading from "@/components/page-loading/page-loading.vue"
Vue.component("pageLoading",pageLoading)

Vue.config.productionTip = false
App.mpType = 'app'

Vue.prototype.$store = store

import mixin from './mixins/index.js';
Vue.mixin(mixin);

Vue.prototype.socket = null

const app = new Vue({
	store,
	...App
})
app.$mount()
// #endif

// #ifdef VUE3
import {
	createSSRApp
} from 'vue'
export function createApp() {
	const app = createSSRApp(App)
	return {
		app
	}
}
// #endif