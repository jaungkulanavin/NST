"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.pageFixture = void 0;
const test_1 = require("@playwright/test");
exports.pageFixture = {
    page: {},
    chromium: test_1.chromium,
    browser: {},
    expect: test_1.expect
};
