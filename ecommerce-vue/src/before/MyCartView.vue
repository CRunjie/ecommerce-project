<template>
     <el-dialog title="我的购物车" v-model="myfocusVisible" width="50%" @close="goClose(1)">
      <el-table :data="goodslists"  border :key="itemKey">
        <el-table-column label="图片">
          <template #default="scope">
            <el-link @click="goToGoodsDetail(scope.row)">
                <el-image  :src="getAssetsFile(scope.row.gpicture)" style="width: 50px; height: 50px;"/>
            </el-link>
          </template>
        </el-table-column>
        <el-table-column  label="商品名称">
            <template #default="scope">
            <el-link @click="goToGoodsDetail(scope.row)" :underline="false">
                <span>{{scope.row.gname}}</span>
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="商品实价">
          <template #default="scope">
            <span>{{scope.row.grprice.toFixed(1)}}</span>
          </template>
        </el-table-column>
        <el-table-column  label="购买量">
            <template #default="scope">
                <el-button size="small" type="success"  @click="reduce(scope.row)" :disabled="scope.row.shopnum === 1">-</el-button>
                <span>&nbsp;{{scope.row.shoppingnum}}&nbsp;</span>
                <el-button size="small" type="success"   @click="add(scope.row)">+</el-button>
             </template>
        </el-table-column>
        <el-table-column  label="小计">
             <template #default="scope">
                <span>{{(scope.row.grprice * scope.row.shoppingnum).toFixed(1)}}</span>
            </template>
        </el-table-column>
        <el-table-column label="删除">
          <template #default="scope">
            <el-row>
              <el-button size="small" type="danger" :icon="Delete" circle  @click="remove(scope.row)"/>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
      <br>
      <div v-if="goodslists.length > 0">总价：¥ {{ totalPrice.toFixed(1) }} &nbsp;
       <el-button  type="success" :icon="ShoppingBag"  @click="goClose(2)">去结算</el-button>
       <el-button  type="danger" :icon="Delete"  @click="removeAll">清空</el-button>
      </div>
  </el-dialog>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import {useRoute, useRouter} from 'vue-router'
import { Delete, ShoppingBag } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from '../axios/request.js'
const router = useRouter()
const route = useRoute() 
const myfocusVisible = ref(true) 
const goodslists = ref([])
let itemKey = ref(0)
//组件初始化
onMounted(() => {
  loadGoods()
})
// 获取assets静态资源
const getAssetsFile = (url) => {
  return new URL(`../assets/${url}`, import.meta.url).href;
}
//加载商品信息
const loadGoods = () => {
  axios.myPost('/api/before/cart/myCart',
  {
    busertableId: sessionStorage.getItem('bid')
  })
  .then(res => {
      goodslists.value =  res.data.resData;
      itemKey.value = Math.random() //刷新表格数据
  })
  .catch((error) => {
      ElMessage.error(error)
  })
}
const goToGoodsDetail = (goods) => {
    router.push({name: 'goodsDetail', state: goods })
}
const goClose = (n) => {
    //修改完购物车后，关闭对话框时批量更新
    let cids = []
    let shoppingnums = []
    for (let i = 0; i < goodslists.value.length; i++) {
        let item = goodslists.value[i]
        cids[i] = item.cid
        shoppingnums[i] = item.shoppingnum
    }
    axios.myPost('/api/before/cart/bupDateCart',
    {
      bcid: cids,
      bshoppingnum: shoppingnums
    })
    //跳转到前一个页面
    let path = route.query.redirect
    if(n === 1)//关闭
      router.replace({ path: path === '/' || path === undefined ? '/' : path })
    else //去结算
      router.push({name: 'goOrder'})
}
//减
const reduce = (goods) => {
    if(goods.shoppingnum === 1) 
        return
    goods.shoppingnum --
}
//加
const add = (goods) => {
    goods.shoppingnum++
}
//删除购物车
const remove = (goods) => {
  axios.myPost('/api/before/cart/removeCart',
    {
      id: goods.cid
    }).then(res => {
      console.log(res.data.msgId)
      ElMessage.success({message: '成功删除购物车！',type: 'success'})
      loadGoods()
    })
}
//使用计算属性计算总额
const totalPrice = computed( ()=> {
    let total = 0
    for (let i = 0; i < goodslists.value.length; i++) {
        let item = goodslists.value[i]
        total = total + item.grprice * item.shoppingnum
    }
    return total
})
//清空购物车
const removeAll = () => {
  axios.myPost('/api/before/cart/clearCart',
    {
      busertableId: sessionStorage.getItem('bid')
    }).then(res => {
      console.log(res.data.msgId)
      ElMessage.success({message: '已清空购物车！',type: 'success'})
      loadGoods()
    })
}
</script>