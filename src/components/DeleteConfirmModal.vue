<script setup>
import { useConfirmDelete } from '@/composables/useConfirmDelete';

const { isVisible, modalTitle, handleConfirm, handleCancel } = useConfirmDelete();
</script>

<template>
    <!-- Delete Confirmation Modal -->
    <div v-if="isVisible" class="fixed inset-0 bg-black/50 flex items-center justify-center z-[9999] p-4">
        <div class="bg-white rounded-xl shadow-2xl max-w-md w-full p-6">
            <div class="flex items-center gap-3 mb-4">
                <div class="bg-red-100 p-2 rounded-full shrink-0">
                    <svg class="w-6 h-6 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
                    </svg>
                </div>
                <h3 class="text-lg font-bold text-gray-900">{{ modalTitle }}</h3>
            </div>
            <div class="flex gap-3 justify-end mt-6">
                <button @click="handleCancel"
                    class="px-4 py-2 rounded-lg border border-gray-300 text-gray-700 hover:bg-gray-50 transition">
                    Cancel
                </button>
                <button @click="handleConfirm"
                    class="px-4 py-2 rounded-lg bg-red-600 text-white hover:bg-red-700 transition font-semibold">
                    Yes, Delete
                </button>
            </div>
        </div>
    </div>
</template>
