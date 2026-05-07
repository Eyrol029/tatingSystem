<script setup>
import { useUserDataStore } from '@/stores/userData'

const userStore = useUserDataStore()
userStore.init() 
</script>

<template>
  <router-view />
</template>

<style scoped></style>