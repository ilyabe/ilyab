// The hamburger menu
var burger = document.getElementById('burger');
var navbar = document.getElementById('navbar')

/**
 * If the hamburger menu is closed, then open it. Otherwise, close it.
 */
burger.onclick = function toggle() {
  
  var isActive = burger.classList.contains("is-active");
  
  if (isActive) {
    burger.classList.remove("is-active");
    navbar.classList.remove("is-active");
  } else {
    burger.classList.add("is-active");
    navbar.classList.add("is-active");
  }
}
