# Components

## Component Hooks

Hint: How you should read "Component Hook"

A component listener creates and initializes the internal object(s) with all parameters of the component.
This initialization can only use data from one component. Thus, it must be self-containable regarding the
save-state of an object. But this is often not the case -> check FamilyHooks.
