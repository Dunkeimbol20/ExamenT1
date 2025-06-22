const camera = new THREE.PerspectiveCamera(
                45, // FOV
                container.clientWidth / container.clientHeight, // aspect
                0.1, // near
                2000 // far
            );
            window.camera = camera; // Asignas a la variable global
            // ... configuras posición y rotación ...