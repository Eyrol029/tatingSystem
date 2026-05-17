const fs = require('fs');

const files = [
    'src/views/uikit/FamilyPlanningAdmission.vue',
    'src/service/FamilyPlanningService.js'
];

for (const file of files) {
    let content = fs.readFileSync(file, 'utf8');
    content = content.replace(/\/api\/familyplanning/g, '/api/FamilyPlanning');
    fs.writeFileSync(file, content);
    console.log(`Replaced in ${file}`);
}
