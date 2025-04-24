const baseConfig = require('./jest.config');

module.exports = {
  ...baseConfig,
  displayName: 'unit-tests',
  testMatch: ['**/*.unit.spec.ts'],
  coverageDirectory: '<rootDir>/coverage/unit',
  collectCoverageFrom: [
    'src/app/**/*.ts',
    '!src/app/**/*.int.spec.ts',
    '!src/app/**/*.module.ts',
    '!src/app/**/*.stories.ts',
  ],
};