<template>
  <div id="main1" style="width: 50%;height:200px;"></div>
  <div id="main2" style="width: 50%;height:200px;"></div>
  <div id="main3" style="width: 50%;height:200px;"></div>
  <div id="main4" style="width: 50%;height:200px;"></div>
</template>

<script setup>
import * as echarts from 'echarts';
import { onMounted} from 'vue';
onMounted(() => {
    main1()
     main2()
})
const main1 = () => {
        var chartDom = document.getElementById('main1');
    var myChart = echarts.init(chartDom);
    myChart.setOption({
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
        data: [150, 230, 224, 218, 135, 147, 260],
        type: 'line'
        }
    ]
    })
}

const main2 = () => {
        var chartDom = document.getElementById('main2');
    var myChart = echarts.init(chartDom);
    myChart.setOption({
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
    yAxis: {
        type: 'value'
    },
    series: [
        {
        data: [150, 230, 224, 218, 135, 147, 260],
        type: 'line'
        }
    ]
    })
}
</script>

<style>

</style>