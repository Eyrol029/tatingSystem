<script setup>
import { useUserDataStore } from '@/stores/userData'
import DeleteConfirmModal from '@/components/DeleteConfirmModal.vue'

const userStore = useUserDataStore()
userStore.init() 
</script>

<template>
  <router-view />
  <DeleteConfirmModal />
</template>
