import { ref } from 'vue';

const isVisible = ref(false);
const modalTitle = ref('Are you sure you want to delete this?');
let resolveFn = null;

export function useConfirmDelete() {
    const confirmDelete = (customTitle) => {
        modalTitle.value = customTitle || 'Are you sure you want to delete this?';
        isVisible.value = true;
        return new Promise((resolve) => {
            resolveFn = resolve;
        });
    };

    const handleConfirm = () => {
        isVisible.value = false;
        if (resolveFn) {
            resolveFn(true);
            resolveFn = null;
        }
    };

    const handleCancel = () => {
        isVisible.value = false;
        if (resolveFn) {
            resolveFn(false);
            resolveFn = null;
        }
    };

    return {
        isVisible,
        modalTitle,
        confirmDelete,
        handleConfirm,
        handleCancel
    };
}
