<template>
	<div id="myChart" :style="{width: '80%', height: '500px'}"></div>
</template>
<script setup>
import { onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts';
import axios from '../axios/request.js'
const mydraw = (result) => {
    const myChart = echarts.init(document.getElementById('myChart'))
    const option = {
        title: {
            text: '按商品分类统计订单',
            subtext: '单位(元)',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left'
        },
        series: [
            {
            name: '订单销量',
            type: 'pie',
            radius: '50%',
            label: {
                formatter: "{b}:  {c|{c}} {per|{d}%}",
                rich: {
                    c: {
                        color: '#4C5058',
                        fontSize: 12,
                        fontWeight: 'bold',
                        lineHeight: 33
                    },
                    per: {
                        color: '#fff',
                        backgroundColor: '#4C5058',
                        padding: [3, 4],
                        borderRadius: 4
                }
                }
            },
            data: result,
            emphasis: {
                itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
            }
        ]
    }
    myChart.setOption(option)
}
onMounted (() => {
    axios.myPost('/api/goods/selectOrderByType')
    .then(res => {
        mydraw(res.data.resData)
    })
    .catch((error) => {
        ElMessage.error(error)
    })
})
</script>
