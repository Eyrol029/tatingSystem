<script setup>
import { useLayout } from '@/layout/composables/layout';

const { toggleMenu } = useLayout();
</script>

<template>
    <div class="layout-topbar">
        <div class="layout-topbar-logo-container">
            <button class="layout-menu-button layout-topbar-action" @click="toggleMenu">
                <i class="pi pi-bars"></i>
            </button>

            <router-link to="/" class="layout-topbar-logo">
                <img src="/static/TATING (2).png" alt="Tating Logo" style="height: 3rem; width: auto; object-fit: contain; vertical-align: middle; margin-right: 0.5rem" />
                <span>Tating Management System</span>
            </router-link>
        </div>

        <div class="layout-topbar-actions">
            <!-- Removed Dark Mode + Color Picker -->

            <button
                class="layout-topbar-menu-button layout-topbar-action"
                v-styleclass="{ selector: '@next', enterFromClass: 'hidden', enterActiveClass: 'animate-scalein', leaveToClass: 'hidden', leaveActiveClass: 'animate-fadeout', hideOnOutsideClick: true }"
            >
                <i class="pi pi-ellipsis-v"></i>
            </button>

            <div class="layout-topbar-menu hidden lg:block">
                <div class="layout-topbar-menu-content">
                    <button type="button" class="layout-topbar-action">
                        <i class="pi pi-user"></i>
                        <span>Profile</span>
                    </button>

                    <button type="button" class="layout-topbar-action" @click="$router.push('/uikit/Login')">
                        <i class="pi pi-sign-out"></i>
                        <span>Logout</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
