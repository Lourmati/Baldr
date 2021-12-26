;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pseudo-licence.                                    ;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


; initialisation des variables
.bss
lecture: resb 64 ; va servir à la lecture
ecriture: resb 64 ; va servir à l'écrture

; code pour la lecture
mov eax, 3 ; registre eax
mov ebx, 0 ; registre ebx
mov ecx, lecture ; registre ecx
mov edx, 64 ; registre edx
int 0x80 ; fin d'instruction


; code pour l'écriture
mov eax, 4 ; registre eax
mov ebx, 0 ; registre ebx
mov ecx, ecriture ; registre ecx
mov edx, 64 ; registre edx
int 0x80 ; fin d'instruction

;;;;;;;;;;;;;;;;;;;;;;;
;; Fin de programme;;;;
;;;;;;;;;;;;;;;;;;;;;;;