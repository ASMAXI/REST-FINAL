// document.addEventListener('DOMContentLoaded', function () {
//     // Load users
//     fetch('/api/admin/users')
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Network response was not ok');
//             }
//             return response.json();
//         })
//         .then(users => {
//             const usersTable = document.getElementById('usersTable');
//             users.forEach(user => {
//                 const row = document.createElement('tr');
//                 row.innerHTML = `
//                     <td>${user.id}</td>
//                     <td>${user.name}</td>
//                     <td>${user.last_name}</td>
//                     <td>${user.age}</td>
//                     <td>${user.email}</td>
//                     <td>${user.roles}</td>
//
//                     <td>
//                         <button class="btn btn-sm btn-primary edit-user" data-id="${user.id}">Edit</button>
//                         <button class="btn btn-sm btn-danger delete-user" data-id="${user.id}">Delete</button>
//                     </td>
//                 `;
//                 usersTable.appendChild(row);
//             });
//         })
//         .catch(error => {
//             console.error('There was a problem with the fetch operation:', error);
//         });
//
//     // Load roles
//     fetch('/api/admin/roles')
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('Network response was not ok');
//             }
//             return response.json();
//         })
//         .then(roles => {
//             const rolesTable = document.getElementById('rolesTable');
//             roles.forEach(role => {
//                 const row = document.createElement('tr');
//                 row.innerHTML = `
//                     <td>${role.id}</td>
//                     <td>${role.name}</td>
//                 `;
//                 rolesTable.appendChild(row);
//             });
//         })
//         .catch(error => {
//             console.error('There was a problem with the fetch operation:', error);
//         });
//
//     // Handle user actions
//     document.addEventListener('click', function (event) {
//         if (event.target.classList.contains('edit-user')) {
//             const userId = event.target.getAttribute('data-id');
//             // Implement edit user logic here
//         } else if (event.target.classList.contains('delete-user')) {
//             const userId = event.target.getAttribute('data-id');
//             fetch(`/api/admin/users/${userId}`, { method: 'DELETE' })
//                 .then(() => {
//                     event.target.closest('tr').remove();
//                 })
//                 .catch(error => {
//                     console.error('There was a problem with the delete operation:', error);
//                 });
//         }
//     });
// });