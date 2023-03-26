# Component specifications

These components of different types describe reach text content that later can be processed by a frontend application.

All components have uniform structure:

- `type` — text field, contains the component type name
- `properties` — a map of properties that alter the component's behavior. All values are stored as Strings, but some values are later parsed by the frontend application, so additional requirements may apply. All components may have the `id` property
- `children` — a list of current component's children. Depending on the component's category, it may require children, or, on the opposite, always be a 'leaf' node

Generally, all components divide in two categories:

- `block`
- `inline`

Different components have their own restrictions on their children type.

## Block components

### Chapter

This component serves as a simple container for other block components and always has a title to describe its content.

**Type:** block

**Children:** block elements, at least one must be provided

**Properties:**

- `title` — `String` — required

### List

A list contains a set of block elements. For the sake of simplicity, let's agree that list can contain them directly.

**Type:** block

**Children:** block elements, at least one

**Properties:**

- `style` — one of `bullet`, `numerical`, `plain` — required
- `startWith` — positive integer — optional, only applicable when `style` is `numerical`
- `bulletType` — one of `circle`, `square` — optional, only applicable when `style` is `bullet`

### Image

Represents an image that may have a light and a dark version. Only few image extensions are allowed: `png`, `jpg`, `jpeg`, `svg`.

**Type:** block

**Children:** none

**Properties:**

- `src` — `String` — required, must contain name of a supported image
- `darkSrc` — `String` — optional, must contain name of a supported image

### Paragraph

Paragraph serves as container for inline components.

**Type:** block

**Children:** inline elements, at least one

**Properties:** none

## Inline components

### Text

Container element for text values.

**Type:** inline

**Children:** none

**Properties:**

- `value` — `String` — required

### Link

This element is used to define links to external resources. Only `http` and `https` protocols are allowed.

**Type:** inline

**Children:** `Text`, at least one

**Properties:**

- `href` — `String` — required, a URL of allowed protocol

### Format

An element to apply formatting to some text.

**Type:** inline

**Children:** `Text`, at least one

**Properties:**

- `style` — one of `bold`, `italic`, `color` — required
- `color` — 6-digit hex color (e.g. `FDB60D`) — is required when `style` is `color`