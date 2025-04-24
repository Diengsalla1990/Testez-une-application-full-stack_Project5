const baseConfig = require('./jest.config');

module.exports = {
  ...baseConfig,
  displayName: 'integration-tests',
  testMatch: ['**/*.int.spec.ts'],
  coverageDirectory: '<rootDir>/coverage/integration',
  collectCoverageFrom: [
    'src/app/**/*.ts',
    '!src/app/**/*.unit.spec.ts',
    '!src/app/**/*.module.ts',
  ],
  setupFilesAfterEnv: ['<rootDir>/setup-jest-integration.ts'],
};