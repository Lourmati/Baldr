
.bss
lecture: resb 64
ecriture: resb 64

mov eax, 3
mov ebx, 0
mov ecx, lecture
mov edx, 64
int 0x80

mov eax, 4
mov ebx, 0
mov ecx, ecriture
mov edx, 64
int 0x80
